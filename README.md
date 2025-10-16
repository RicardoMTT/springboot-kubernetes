# 🚀 Arquitectura de Microservicios: User-Service & Order-Service en Kubernetes

Este repositorio contiene la configuración y la documentación de una arquitectura de microservicios simple desplegada en un clúster de Kubernetes (K8s), utilizando **Spring Boot** para el desarrollo de la lógica de negocio.

La arquitectura se compone de dos servicios principales: un **Servicio de Usuarios** (`user-service`) y un **Servicio de Órdenes** (`order-service`), los cuales se comunican internamente utilizando el sistema de Service Discovery de Kubernetes.

---

## 🏗️ 1. Visión General de la Arquitectura

El despliegue está orquestado por un único archivo de manifiesto (`.yml`) que define la estructura de alta disponibilidad y la conectividad para ambos microservicios.

| Microservicio | Puerto Interno (TargetPort) | Rol Principal | Dependencias |
| :--- | :--- | :--- | :--- |
| **`user-service`** | 8081 | Gestiona la lógica de datos de usuarios. | Ninguna (Proveedor de datos) |
| **`order-service`** | 8081 | Gestiona la lógica de órdenes. | **Depende de `user-service`** (mediante Feign Client) |

### Patrón de Comunicación (Service Discovery)

El `order-service` utiliza el nombre del servicio de Kubernetes (`user-service`) para realizar llamadas HTTP (a través de Feign Client) a `http://user-service:8081`.

**`order-service` (Pod) ➡️ DNS K8s ➡️ `user-service` (Service) ➡️ `user-service` (Pod)**

Esto garantiza que la comunicación sea estable y se beneficie del balanceo de carga automático de Kubernetes.

---

## 📦 2. Componentes de Kubernetes

El manifiesto de Kubernetes utiliza dos tipos de recursos fundamentales para cada microservicio: **Deployment** y **Service**.

### 2.1. Deployment (`Deployment`)

El Deployment es responsable de mantener el estado deseado de la aplicación (la cantidad de réplicas).

- **Réplicas (`replicas: 2`):** Se mantienen dos instancias (Pods) de cada microservicio en ejecución. Si un Pod falla, Kubernetes lo reinicia automáticamente.
- **Chequeos de Salud (Probes):**
    - **`livenessProbe`:** Verifica que la aplicación esté viva. Si falla (por ejemplo, por un *deadlock* o error interno), el Pod se reinicia.
    - **`readinessProbe`:** Verifica que la aplicación esté lista para manejar tráfico. Si falla, el Pod se mantiene en ejecución, pero se aísla del balanceo de carga hasta que se recupere.

### 2.2. Service (`Service`)

El Service (`type: ClusterIP`) proporciona una identidad de red estable a las réplicas del Deployment.

- **Tipo `ClusterIP`:** El servicio es accesible únicamente desde otros Pods dentro del clúster.
- **Balanceo de Carga:** El Service actúa como un balanceador de carga virtual, distribuyendo el tráfico entrante de `user-service:8081` a las dos réplicas del Pod de usuarios.
- **`targetPort: 8081`:** Confirma que la aplicación Spring Boot dentro del contenedor escucha en el puerto 8081.

---

## 🛠️ 3. Configuración y Pruebas Locales (Minikube)

Para desplegar y probar esta arquitectura en un entorno de desarrollo local (como Minikube):

### 3.1. Prerrequisitos

1.  **Imágenes de Docker:** Las imágenes `user-service:1.0.0` y `order-service:1.0.0` deben estar disponibles en el registro de Docker de Minikube.
    ```bash
    # 1. Apuntar Docker a Minikube
    minikube docker-env | Invoke-Expression 
    # 2. Construir las imágenes
    docker build -t user-service:1.0.0 ./user-service
    docker build -t order-service:1.0.0 ./order-service
    ```

### 3.2. Despliegue

Aplica el manifiesto YAML a tu clúster de Kubernetes:

```bash
kubectl apply -f [nombre-del-archivo].yml
