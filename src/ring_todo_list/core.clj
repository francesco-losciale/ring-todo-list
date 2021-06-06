(ns ring-todo-list.core
  (:require [muuntaja.core :as m]
            [reitit.coercion.schema]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring :as reitit]
            [reitit.ring.coercion :as coercion]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.util.response :as http]
            [schema.core :as s]
            [ring-todo-list.db :as db]
            ))

; TODO add oauth2

(def app
  (let [conn (atom (db/db-connection!))
        transform-for-view #(update % :_id str)]
    (reitit/ring-handler
      (reitit/router
        [
         ["" {:no-doc true}
          ["/swagger.json"
          {:get (swagger/create-swagger-handler)}]
         ["/swagger-ui*"
          {:get (swagger-ui/create-swagger-ui-handler {:url "/swagger.json"})}]]
         ["/api/v1/todo-lists"
          [""
           {
            :post
            {:summary "Create a new todo list"
             :parameters {:body {:todo-list [{:id  s/Int :text s/Str}]}}
             :responses {201 {:body {:_id s/Str :todo-list [{:id  s/Int :text s/Str}]}}}
             :handler
             (fn [{todo-list :body-params}]
               (http/created
                 ""
                 (transform-for-view
                   (db/insert-todo-list! @conn todo-list))))
             }

            :get
            {:summary "Get all the todo lists"
             :responses {200 {:body [{:_id s/Str :todo-list [{:id  s/Int :text s/Str}]}]}}
             :handler
             (fn [_]
               (http/response
                 (map transform-for-view (db/get-all))))
             }
            }]

          ["/:id"
           {
            :get
            {:parameters {:path {:id s/Str}}
             :summary "Get specific todo lists"
             :responses {200 {:body {:_id s/Str :todo-list [{:id  s/Int :text s/Str}]}}}
             :handler
                         (fn [{:keys [parameters]}]
                           (let [id (-> parameters :path :id)]
                             (http/response
                               (transform-for-view
                                 (db/get-one @conn id)))
                             ))
             }}]
          ]]


        {:data {
                :coercion reitit.coercion.schema/coercion
                :muuntaja   m/instance
                :middleware [muuntaja/format-middleware
                             [wrap-cors :access-control-allow-origin [#".*"] ; <-- order matters
                              :access-control-allow-methods [:get :post]]
                             coercion/coerce-exceptions-middleware
                             coercion/coerce-request-middleware
                             coercion/coerce-response-middleware]
                }}))))

(defn -main [& args]
  (run-jetty
    (-> app
        var
        wrap-reload)
    {:port 3000}))