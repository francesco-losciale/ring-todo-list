(ns ring-todo-list.db
  (:require [monger.core :as mg]
            [monger.credentials :as mcr]
            [monger.collection :as mc])
  (:import org.bson.types.ObjectId))

; TODO it is recommended to use ObjectId so that your documents are immutable

(defn db-connection! []
  (let [cred (mcr/create "todo-lists" "todo-lists" "example")
        conn (mg/connect-with-credentials "localhost" 27017 cred)]
    conn))

(defn insert-todo-list! [conn todo-list]
  (let [db (mg/get-db conn "todo-lists")]
    (mc/insert-and-return db "collection" todo-list)
    ))

(defn update-todo-list! [conn  ^String id todo-list]
  (let [db (mg/get-db conn "todo-lists")]
    (mc/update-by-id db "collection" (ObjectId. id) todo-list)
    ))

(defn get-all []
  ; find-maps closes the connection so you don't need to close it outside.
  ; This is the reason `conn` is not passed as a value to the function.
  ; https://github.com/michaelklishin/monger/pull/47 That was the bug it was fixing.
  ; Previously, if you lazily consumed just the first portion of the result (from
  ; either find-maps or find-seq), then the connection was never explicitly freed up
  ; (because you didn’t reach the end of the sequence).
  ; (it’s why laziness + side-effects = trouble in most cases)
  (mc/find-maps (mg/get-db (db-connection!) "todo-lists") "collection"))

(defn get-one [conn ^String object-id]
  (mc/find-one-as-map
    (mg/get-db conn "todo-lists") "collection"
    {:_id (ObjectId. object-id)}))

(defn close! [conn]
  (mg/disconnect conn))

(comment
  (let [conn (db-connection!)]
    (insert-todo-list! conn)
    (close! conn))
  (let [conn (db-connection!)]
    (update-todo-list! conn "60d07d45ecf71b07c688e5f7" {:todo-list [{:text "sdfdg", :id 3} {:text "asdf", :id 2} {:text "asdf", :id 1}]})
    ;(close! conn)
    )
  (let [conn (db-connection!)]
    (get-one conn "60d07d45ecf71b07c688e5f7")
    ;(close! conn)
    )
  (let [conn (db-connection!)]
    (mc/find-maps (mg/get-db conn "todo-lists") "collection"))
  (let [conn (db-connection!)]
    (mc/find-one-as-map (mg/get-db conn "todo-lists") "collection" {:_id (ObjectId. "60ab92b90b6615f55ab0b21c")}))
  )