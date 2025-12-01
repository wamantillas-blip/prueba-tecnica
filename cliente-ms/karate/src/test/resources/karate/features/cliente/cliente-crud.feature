Feature: Gestión de Clientes - Prueba de Aceptación

  Background:
    * url 'http://localhost:8081/clientes'

  Scenario: Flujo completo de creación, actualización y eliminación de clientes


    # ===== CREAR CLIENTES =====
    * def cliente1 =
      """
      {
        "nombre": "Marianela Montalvo",
        "genero": "Femenino",
        "edad": 35,
        "identificacion": "5555522522444433331",
        "direccion": "Amazonas y NNUU",
        "telefono": "097548965",
        "contrasena": "5678",
        "estado": true
      }
      """
    Given request cliente1
    When method POST
    Then status 201
    * def clienteId1 = response.clienteId


    # ===== OBTENER CLIENTE POR ID =====
    Given path clienteId1
    When method GET
    Then status 200
    And match response.clienteId == clienteId1
    And match response.nombre == 'Marianela Montalvo'

    # ===== ACTUALIZAR CLIENTE =====
    * def updateCliente1 =
      """
      {
        "nombre": "Wilmer",
        "genero": "M",
        "edad": 33,
        "identificacion": "5555522522444433331",
        "direccion": "NUEVA DIRECCION ACTUALIZADA",
        "telefono": "0999888722277",
        "contrasena": "claveNueva",
        "estado": true
      }
      """
    Given path clienteId1
    And request updateCliente1
    When method PUT
    Then status 200
    And match response.direccion == 'NUEVA DIRECCION ACTUALIZADA'
    And match response.telefono == '0999888722277'


    # ===== ELIMINAR CLIENTE =====
    Given path clienteId1
    When method DELETE
    Then status 204
