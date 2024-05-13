# Kubernetes Experiment

Roelof's Kubernetes experiment.

My goal for today is to get a simple web application running in a Kubernetes
cluster.
The application consists of a frontend, stupid simple backend, and a database.
This is a simple CRUD application.

## License

[General Public License v3](LICENSE)

## To Do List

- [x] Setup frontend using Nuxt.js
- [x] Setup backend in Spring Boot
- [x] Figure out how to deploy in K8s
- [x] Add Kubernetes configuration
  - Namespace
  - Deployments
  - Services
  - Ingress route
- [x] Test Kubernetes configuration in CI
- [ ] Make it work

## Running locally

You should be able to cleanly install the frontend and backend applications
using their respective README files.

To run everything in your local Kubernetes cluster (like `minikube`), you should
do the following:

### Create a local registry token

Make a GitHub PAT with the `packages:read` scope to pull the images here, and
insert it as as Kubernetes secret.

```bash
kubectl create secret docker-registry ghcr \
    --docker-server=ghcr.io \
    --docker-username=<your_username> \
    --docker-password=<your_github_pat>
```

Alternatively, if you have the GitHub CLI installed, you can use its token.

```bash
kubectl create secret docker-registry ghcr \
    --docker-server=ghcr.io \
    --docker-username=$( gh api --jq .login /user ) \
    --docker-password=$( gh auth token )
```

### Deploy the applications

All required configuration for the Kubernetes cluster is in the `k8s` directory.
You can simply roll that out to get a somewhat functional setup.

```bash
kubectl apply --recursive -f k8s
```
