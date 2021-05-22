# ring-todo-list

POST todo-list
`curl -H "Content-Type: application/json" --data '{"id":1,"todo-list":[{"id":1,"text":"Do something"}]}' -X POST localhost:3000/api/v1/todo-lists`

GET todo-list by id
`curl localhost:3000/api/v1/todo-lists/3`

GET all todo lists
`curl localhost:3000/api/v1/todo-lists`