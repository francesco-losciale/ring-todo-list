(ns ring-todo-list.core
  (:require [reitit.ring :as ring]
            [reitit.ring.coercion :as coercion]
            [reitit.coercion.schema]
            [schema.core :as s]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.util.response :as http]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [muuntaja.core :as m]
            [ring-todo-list.db :as db]
            ))

(def app
  (let [conn (atom (db/db-connection!))]
    (ring/ring-handler
     (ring/router
       [
        ["/api/v1/todo-lists"
         [""
          {:post
           {:handler
            (fn [{todo-list :body-params}]
              (http/created "" (db/insert-todo-list! @conn todo-list)))
            }
           :get
           {:handler
            (fn [_]
              (http/response (db/get-all)))
            }
           }]
         ["/:id"
          {:get
           {:coercion   reitit.coercion.schema/coercion
            :parameters {:path {:id s/Str}}
            :handler
                        (fn [{:keys [parameters]}]
                          (let [id (-> parameters :path :id)]
                            (http/response (db/get-one @conn id))
                            ))
            }}]
         ]
        ]

       {:data {
               :muuntaja   m/instance
               :middleware [muuntaja/format-middleware
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