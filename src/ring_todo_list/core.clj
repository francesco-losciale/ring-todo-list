(ns ring-todo-list.core
  (:require [reitit.ring :as ring]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.adapter.jetty :refer [run-jetty]]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [muuntaja.core :as m]
            ))

(def app
  (ring/ring-handler
    (ring/router
      [
       ["/api/v1/todo-list"
        [""
         {:post
          {:handler
           (fn [{{:keys [todo-list]} :body-params}]         ;{{:keys [x y]} :body-params}
             {:status 200
              :body   todo-list
              })
           }
          :get
          {:handler
           (fn [_]
             {:status 200
              :body   {:todo-list [{:id 2 :text "Do something"}]}
              })
           }
          }]
        ["/:id"
         {:get
         {:handler
          (fn [{{:keys [id]} :path-params}]
            {:status 200
             :body   {:id id :todo-list [{:id 2 :text "Do something else"}]}
             })}}]
        ]
       ]

      {:data {
              :muuntaja   m/instance
              :middleware [muuntaja/format-middleware]
              }})))

(defn -main [& args]
  (run-jetty
    (-> app
        var
        wrap-reload)
    {:port 3000}))