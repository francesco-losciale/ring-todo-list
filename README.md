# ring-todo-list

Curl command to post todo list
`curl -H "Content-Type: application/json" --data '{"todo-list":[{"id":1,"text":"Do something"}]}' -X POST localhost:3000/api/v1/todo-list`