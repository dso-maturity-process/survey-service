#!/bin/bash

kubectl apply -f minikube-deployment.yaml

kubectl rollout restart deployment/survey-service-app -n dmp
