# export VAULT_TOKEN="my-token"
# export VAULT_ADDR="http://127.0.0.1:8200"

#########################################

start-all: up write-kv create-encryption-key setup-db-and-roles create-username-kv

#########################################

up:
	docker-compose up -d
	sleep 20
	./vault secrets enable transit
	./vault secrets enable database
	./vault secrets enable kv

down:
	docker-compose down
	docker container prune -f
	docker volume prune -f

#########################################

write-kv:
	./vault kv put secret/my-secret my-value="$(DATA)"

read-kv:
	./vault kv get secret/my-secret

create-username-kv:
	./vault kv put secret/gs-vault-config example.username=demouser example.password=demopassword
	./vault kv put secret/gs-vault-config/cloud example.username=clouduser example.password=cloudpassword

#########################################

create-encryption-key:
	./vault write -f transit/keys/my-key

encrypt-data:
	./vault write transit/encrypt/my-key plaintext=$(DATA)

decrypt-data:
	./vault write transit/decrypt/my-key ciphertext=$(DATA)

#########################################

setup-db:
	./vault write database/config/db1 plugin_name=mysql-database-plugin connection_url="{{username}}:{{password}}@tcp(mysql:3306)/" allowed_roles="read-only,read-write" username="root" password="example"

create-db-user:
	./vault write database/roles/read-only db_name=db1 creation_statements="CREATE USER '{{name}}'@'%' IDENTIFIED BY '{{password}}'; GRANT SELECT ON *.* TO '{{name}}'@'%';" default_ttl="3m" max_ttl="3m"

create-db-user-rw:
	./vault write database/roles/read-write db_name=db1 creation_statements="CREATE USER '{{name}}'@'%' IDENTIFIED BY '{{password}}'; GRANT ALL PRIVILEGES ON *.* TO '{{name}}'@'%';" default_ttl="3m" max_ttl="3m"

setup-db-and-roles: setup-db create-db-user create-db-user-rw

get-db-creds:
	./vault read database/creds/read-only

get-db-creds-rw:
	./vault read database/creds/read-write

login-db:
	mysql -h 127.0.0.1 -u $(USER) -p

#########################################

read-config:
	curl http://localhost:8080/api/config

read-actors:
	curl http://localhost:8080/api/actors

write-actor:
	curl -X POST http://localhost:8080/api/actors

refresh:
	curl -X POST http://localhost:8080/actuator/refresh

