import socket

cliente = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
cliente.connect(("127.0.0.1", 12345))
print("Conexion establecida con el servidor local")

nombre= input("Tu nombre: ")
correo= input("Tu correo: ")

mensaje =f"{nombre}, {correo}"

cliente.send(mensaje.encode())
respuesta = cliente.recv(1024).decode()
print("Respuesta del servidor:", respuesta)
cliente.close()