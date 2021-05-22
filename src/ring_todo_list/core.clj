(ns ring-todo-list.core
  (:require [reitit.ring :as ring]
            [ring.adapter.jetty :refer [run-jetty]]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [muuntaja.core :as m]
            ))

(def app
  (ring/ring-handler
    (ring/router
      [["/api/hello" {:get  {
                             :handler
                             (fn [request]
                               {:status  200
                                :headers {"Content-Type" "text/plain"}
                                :body    "Hello World"})
                             }
                      :post {
                             :handler
                             (fn [request]
                               {:status  200
                                :headers {"Content-Type" "text/plain"}
                                :body    (str (:body-params request))})
                             }}]
       ["/api/v1/todo-list"
        {:post {
                :handler
                (fn [request]
                  {:status 200
                   :body   {:todo-list [{:id 1 :text "Do something"}]}
                   })
                }
         }]]

      {:data {
              :muuntaja   m/instance
              :middleware [muuntaja/format-middleware]
              }})))

(defn -main [& args]
  (run-jetty app {:port 3000}))