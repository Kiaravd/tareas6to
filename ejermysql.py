import mysql.connector 
from mysql.connector import Error

def crear_conexion():
    conexion = None
    try:
        conexion = mysql.connector.connect(
            host='localhost',
            user='root',
            password='',
            database= "servidor")
        print('La conexion fue exitosa')
    except Error as e:
        print(f"Falló la conexion a MYSQL:{e}")
    return conexion

def mostrar_datos(conexion):
    try:
        cursor= conexion.cursor()
        query = "SELECT nombre, correo FROM usuarios"
        cursor.execute(query)
        datos = cursor.fetchall()
        for row in datos:
            print(f"Nombre:{row[0]}, correo:{row[1]}")
    except Error as e:
        print(f"falló el select por: {e}")

mostrar_datos(crear_conexion())