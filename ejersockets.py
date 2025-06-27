import socket
import sys
import mysql.connector
from mysql.connector import Error

# hostname= socket.gethostname()
# ip = socket.gethostbyname(hostname)

# print(f"El nombre del host es: {hostname}")
# print (f"La ip del host es: {ip}")

def conexion_mysql():
    conexion = None
    try:
        conexion = mysql.connector.connect(
            user = "root",
            host = "localhost",
            password = "",
            database = "servidor")
        print("Se conectó con exito")
    except Error as e:
        print(f"Fallo la conexion: {e}")
    return conexion

def insertar_datos(conexion, tupla):
    datos = None
    nombre , correo = tupla
    try:
        cursor = conexion.cursor()
        query = "INSERT INTO usuarios (nombre, correo) VALUES (%s, %s) "
        cursor.execute(query, (nombre, correo))
        conexion.commit()
        print("insercion exitosa")
        query = "SELECT * FROM usuarios"
        cursor.execute(query)
        datos = cursor.fetchall()
        print("busqueda exitosa")
    except Error as e:
        print(f"Fallo por: {e}")
    return datos

def mostrar_datos(datos):
    for row in datos:
        print(f"nombre: {row[1]} y correo: {row[2]}")
    

server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.bind(("127.0.0.1",12345))
server_socket.listen(1)

if server_socket:
    print("servidor escuchando en el puerto 12345")

datos = None
while True:
    client_socket, address = server_socket.accept()
    print(f"Se establecio la conexion {str(address)}")
    datos= client_socket.recv(1024).decode()
    print(f"Datos recibidos: {datos}")
    respuesta = "Gracias, tus datos se recibieron correctamente"
    client_socket.send(respuesta.encode())
    client_socket.close()
    break


tupla = tuple(datos.split(","))
validacion = conexion_mysql()
info = insertar_datos( validacion, tupla)
mostrar_datos (info)
