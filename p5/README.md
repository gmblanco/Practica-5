# P5
Aplicación web que usa Spring JPA para persistir los datos de un API REST de gestión de usuarios.
El API permite el registro de nuevos usuarios y su identificación mediante email y password.
Una vez identificados, se emplea una cookie de sesión para autenticar las peticiones que permiten 
a los usuarios leer, modificar y borrar sus datos. También existe un endpoint para cerrar la sesión.  

## Endpoints

// TODO#1: rellena la tabla siguiente analizando el código del proyecto

| Método | Ruta                  | Descripción                                                                                                                                                                                                                                                                                                                                                                                                                         | Respuestas                                                                                                                                                                    |
|--------|-----------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| POST   | /api/users            | El método register recibe por parámetro una RegisterRequest con la información del nuevo usuario a registrar. Primero, intenta devolver la respuesta con el perfil del nuevo usuario, y lo registra en la base de datos. Y si se intenta registrar un usuario que ya figura en la base de datos, entonces lanza una excepción CONFLICT, indicando que no es posible registrar dos veces a un mismo usuario.                         | Si todo va bien, devuelve respuesta con perfil del usuario. Si se intenta registrar a un usuario ya registrado, se recibe mensaje de error "Status = 409".                    |
| POST   | /api/users/me/session | El método login recibe por parámetro una LoginRequest donde se espera recibir un email y contraseña para iniciar sesión. Para ello se comunica con el service y obtiene el token asociado al usuario con esas credenciales. Si esas credenciales son incorrectas, el token recibido es nulo y lanza una excepción UNATHORIZED, mientras que si son correctas, crea una cookie de sesión y la devuelve en el cuerpo de la respuesta. | Devuelve un HTTP 201 CREATED con la cookie de sesión asociada al usuario loggeado.                                                                                            |
| DELETE | /api/users/me/session | El método logout recibe por parámetro la cookie de sesión asociada al usuario. Obtiene el usuario asociado a dicha sesión, verifica si es nulo (en el caso de que no tenga token, lanza una excepción UNAUTHORIZED). Por último, borra el token asociado al usuario en base de datos junto con la cookie de sesión.                                                                                                                 | Devuelve un HTTP 204, donde se indica que se ha borrado la session asociada al usuario que ha hecho el log out.                                                               |
| GET    | /api/users/me         | El método profile recibe por parámetro la cookie de sesión asociada al usuario. Obtiene el usuario asociado a dicha sesión, verifica si es nulo (en el caso de que no tenga token, lanza una excepción UNAUTHORIZED).Por último, devuelve la respuesta con el perfil de dicho usuario.                                                                                                                                              | Devuelve un HTTP 200 con la respuesta con el perfil de dicho usuario. En el cuerpo de la respuesta se puede ver el nombre, email y role del usuario.                          |
| PUT    | /api/users/me         | El método update recibe por parámetro la cookie de sesión asociada al usuario junto con los datos actualizados del usuario que se desean modificar (nuevo perfil del usuario). btiene el usuario asociado a dicha sesión, verifica si es nulo (en el caso de que no tenga token, lanza una excepción UNAUTHORIZED). Por último, devuelve la respuesta con el perfil actualizado de dicho usuario.                                   | Devuelve un HTTP 200 con la respuesta con el perfil actualizado de dicho usuario. En el cuerpo de la respuesta se puede ver el nombre, email y role actualizados del usuario. |
| DELETE | /api/users/me         | El método delete recibe por parámetro la cookie de sesión asociada al usuario. Obtiene el usuario asociado a dicha sesión, verifica si es nulo (en el caso de que no tenga token, lanza una excepción UNAUTHORIZED). Por último, borra en base de datos el usuario, dándolo de baja definitivamente y borrando en cascada su token activo asociado.                                                                                 | Devuelve un HTTP 204, donde se indica que la conexión se ha cerrado. En base de datos se puede comprobar que ha borrado tanto el usuario como su token asociado.              |


## Comandos 

- Construcción: 
  ```sh
  ./mvnw clean package
  ```

- Ejecución: 
  ```sh
  ./mvnw spring-boot:run
  ```

- Tests:
  ```sh
  ./mvnw test
  ```
