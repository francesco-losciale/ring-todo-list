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
  (ring/ring-handler
    (ring/router
      [
       ["/api/v1/todo-lists"
        [""
         {:post
          {:handler
           (fn [{todo-list :body-params}]
             (let [conn (db/db-connection!)]
               (try
                 (http/created "" (db/insert-todo-list! conn todo-list))
                 (finally (db/close! conn)))
               ))
           }
          :get
          {:handler
           (fn [_]
             {:status 200
              :body   [{:id 1 :todo-list [{:id 2 :text "Do something"}]}]
              })
           }
          }]
        ["/:id"
         {:get
          {:coercion   reitit.coercion.schema/coercion
           :parameters {:path {:id s/Int}}
           :handler
                       (fn [{:keys [parameters]}]
                         (let [id (-> parameters :path :id)]
                           {:status 200
                            :body   {:id id :todo-list [{:id 2 :text "Do something else"}]}
                            }))}}]
        ]
       ]

      {:data {
              :muuntaja   m/instance
              :middleware [muuntaja/format-middleware
                           coercion/coerce-exceptions-middleware
                           coercion/coerce-request-middleware
                           coercion/coerce-response-middleware]
              }})))

(defn -main [& args]
  (run-jetty
    (-> app
        var
        wrap-reload)
    {:port 3000}))