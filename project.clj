(defproject ring-todo-list "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring "1.9.3"]
                 [ring-oauth2 "0.1.5"]
                 [ring/ring-jetty-adapter "1.9.3"]
                 [metosin/reitit "0.5.13"]
                 [com.novemberain/monger "3.1.0"]]
  :repl-options {:init-ns ring-todo-list.core}

  :main ^:skip-aot ring-todo-list.core
  )
