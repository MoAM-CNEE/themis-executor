.PHONY: help

DOCKERHUB_USERNAME=proman3419
IMAGE_NAME=themis-executor
# w.x.y.z, one digit value each
# when tinkering add -<description> suffix
VER=0.0.0.1
# 0.0.0.1 (MoAM-CNEE) is based on 0.0.4.4 (k8loud)
DEPLOY_NAMESPACE=themis-executor

# targets that aren't annotated with ## are not supposed to be run on their own

help: ## show Makefile contents
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

build: ## build - image accessible through local repository
	mvn package spring-boot:repackage -DthemisExecutorVersion=$(VER) -DskipTests=true
	docker build -t $(DOCKERHUB_USERNAME)/$(IMAGE_NAME):$(VER) . --build-arg VER=$(VER)

build-jar: ## build a plain jar
	@echo "VER: $(VER)"
	mvn clean install -DthemisExecutorVersion=$(VER)

# GH - GitHub, DH - Dockerhub
# if 409 Conflict is received it means that a package with version VER is already present on GH
# remove it manually:
# https://github.com/MoAM-CNEE/themis-executor/packages -> Package settings -> Manage versions -> delete concrete version
# or change VER

build-and-push-final: ## !RUN ONLY ON MASTER! build-and-push-non-final + tag the commit with VER
	make build-and-push-non-final
	git tag v$(VER)
	git push origin --tags

build-and-push-non-final: ## plain jar -> GH | Docker image from a jar with Spring Boot wrapper -> DH
	make build-jar
	mvn deploy -DthemisExecutorVersion=$(VER) -DskipTests=true
	make build-and-push-docker FULL_IMAGE_NAME=$(DOCKERHUB_USERNAME)/$(IMAGE_NAME):$(VER)

build-and-push-docker:
	@echo "FULL_IMAGE_NAME: $(FULL_IMAGE_NAME)"
	mvn package spring-boot:repackage -DthemisExecutorVersion=$(VER) -DskipTests=true
	docker build -t $(FULL_IMAGE_NAME) . --build-arg VER=$(VER)
	docker login
	docker push $(FULL_IMAGE_NAME)

deploy: ## deploy to the Kubernetes cluster
	kubectl -n $(DEPLOY_NAMESPACE) create secret generic themis-secrets-k8s \
		--from-file=KUBERNETES_CA_CERT_DATA=secrets/ca.crt \
		--from-file=KUBERNETES_CLIENT_CERT_DATA=secrets/client.crt \
		--from-file=KUBERNETES_CLIENT_KEY_DATA=secrets/client.key
	kubectl -n $(DEPLOY_NAMESPACE) create secret generic themis-secrets \
		--from-file=MAIL_PASSWORD=secrets/mail-password.txt \
		--from-file=OPENSTACK_PASSWORD=secrets/openstack-password.txt
	kubectl apply -f manifests/

.DEFAULT_GOAL := help
