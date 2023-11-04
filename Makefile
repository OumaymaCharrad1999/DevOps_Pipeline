export MYSQLDB_USER := root
export MYSQLDB_ROOT_PASSWORD := oumayma
export MYSQLDB_DATABASE := pet_store
export MYSQLDB_LOCAL_PORT := 3306
export MYSQLDB_DOCKER_PORT := 3306
export SPRING_LOCAL_PORT := 3001
export SPRING_DOCKER_PORT := 8080

run_app :
	docker-compose up
stop_app :
	docker-compose down