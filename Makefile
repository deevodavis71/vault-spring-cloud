setup-vault:
	./vault secrets enable transit
	./vault secrets enable database
	./vault secrets enable kv

write-kv:
	./vault kv put secret/my-secret my-value="$(DATA)"

read-kv:
	./vault kv get secret/my-secret

create-encryption-key:
	./vault write -f transit/keys/my-key

encrypt-data:
	./vault write transit/encrypt/my-key plaintext=$(DATA)

decrypt-data:
	./vault write transit/decrypt/my-key ciphertext=$(DATA)

setup-db:
	./vault write database/config/db1 plugin_name=mysql-database-plugin connection_url="{{username}}:{{password}}@tcp(127.0.0.1:3306)/" allowed_roles="read-only,read-write" username="root" password="example"

create-db-user:
	./vault write database/roles/read-only db_name=db1 creation_statements="CREATE USER '{{name}}'@'%' IDENTIFIED BY '{{password}}'; GRANT SELECT ON *.* TO '{{name}}'@'%';" default_ttl="1m" max_ttl="1m"

create-db-user-rw:
	./vault write database/roles/read-write db_name=db1 creation_statements="CREATE USER '{{name}}'@'%' IDENTIFIED BY '{{password}}'; GRANT ALL PRIVILEGES ON *.* TO '{{name}}'@'%';" default_ttl="1m" max_ttl="1m"

setup-db-and-roles: setup-db create-db-user create-db-user-rw

get-db-creds:
	./vault read database/creds/read-only

get-db-creds-rw:
	./vault read database/creds/read-write

login-db:
	mysql -h 127.0.0.1 -u $(USER) -p

all-db: setup-vault setup-db-and-roles
