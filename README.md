# ring-todo-list

POST todo-list
`curl -H "Content-Type: application/json" --data '{"todo-list":[{"id":1,"text":"Do something"}]}' -X POST localhost:3000/api/v1/todo-lists`

GET todo-list by id
`curl localhost:3000/api/v1/todo-lists/60ab92b90b6615f55ab0b21c`

GET all todo lists
`curl localhost:3000/api/v1/todo-lists`

# TODO
- change front end to call service api (use ajax.core)
- set up keycloak
- try https://github.com/jgrodziski/keycloak-clojure follow bookmarks you set in their repo

- go through this guide, add code style, ci, design journal
https://practical.li/clojure/repl-driven-devlopment.html
