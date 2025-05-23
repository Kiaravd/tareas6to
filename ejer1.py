# Instalar el conector de MySQL para Python
# pip install mysql-connector-python
# CREATE TABLE usuarios (
#     id INT AUTO_INCREMENT PRIMARY KEY,
#     nombre VARCHAR(255),
#     email VARCHAR(255),
#     created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
#     updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
# );

# created_at: se llena automáticamente al crear un registro.
# updated_at: se actualiza automáticamente cuando el registro cambia.

import mysql.connector
from mysql.connector import Error

# Función para crear la conexión
def crear_conexion():
    conexion = None
    try:
        conexion = mysql.connector.connect(
            host='localhost',
            user='root',
            password='',  
            database='nombre' 
        )
        print("Conexión exitosa a la base de datos")
    except Error as e:
        print(f"Falló la conexión a MySQL: {e}")
    return conexion

# Función para mostrar los datos de la tabla 'usuarios'
def mostrar_datos(conexion, nickname):  # Aunque no se usa 'nickname'
    try:
        cursor = conexion.cursor()
        query_select = "SELECT * FROM usuarios"
        cursor.execute(query_select)
        datos = cursor.fetchall()  # Trae todos los resultados
        for row in datos:
            print(f"id: {row[0]}, nombre: {row[1]}, email: {row[2]}")  # Ajusta según columnas reales
    except Error as e:
        print(f"Falló la búsqueda SELECT: {e}")
