.PHONY: all frontend backend build push deploy

all: frontend backend build

releaseNumber := $(shell date +%H%M%S)

frontend:
	cd frontend \
		&& npm install \
		&& npm run build
	test -d dist/frontend && rm -rf dist/frontend/* || mkdir -p dist/frontend
	mv frontend/.output/* dist/frontend

backend:
	cd backend \
		&& mvn clean package
	test -d dist/backend && rm -rf dist/backend/* || mkdir -p dist/backend
	mkdir -p dist/backend
	mv backend/target/backend-*.jar dist/backend/app.jar

build:
	docker build -t ghcr.io/roelofr/k8s-experiment/app-frontend:latest -f configs/frontend.dockerfile dist/frontend/
	docker build -t ghcr.io/roelofr/k8s-experiment/app-backend:latest -f configs/backend.dockerfile dist/backend/

push:
	docker push ghcr.io/roelofr/k8s-experiment/app-frontend:latest
	docker push ghcr.io/roelofr/k8s-experiment/app-backend:latest

deploy:
	kubectl create deployment k8s-frontend --image=ghcr.io/roelofr/k8s-experiment/app-frontend:latest
	kubectl create deployment k8s-backend --image=ghcr.io/roelofr/k8s-experiment/app-backend:latest

teardown:
	kubectl get deployments -o name | grep '^deployment.apps/k8s-' | xargs kubectl delete
