(ns ring-todo-list.core
  (:require [reitit.ring :as ring]
            [ring.adapter.jetty :refer [run-jetty]]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [muuntaja.core :as m]
            ))

(def app
  (ring/ring-handler
    (ring/router
      ["/api/hello" {:get  {
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
      {:data {
              :muuntaja m/instance
              :middleware [muuntaja/format-middleware]
              }})))

(defn -main [& args]
  (run-jetty app {:port 3000}))