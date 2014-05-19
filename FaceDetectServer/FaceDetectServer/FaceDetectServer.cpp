#include<io.h>
#include<stdio.h>
#include<winsock2.h>
#include<conio.h>
 
#pragma comment(lib,"ws2_32.lib") //Winsock Library
 
int main(int argc , char *argv[])
{
    WSADATA wsa;
    SOCKET socket1, socket2, new_socket1, new_socket2;
    struct sockaddr_in server, server2, client, client2;
    int c, c2;


    printf("\nInitializing Winsock...");
    if (WSAStartup(MAKEWORD(2, 2), &wsa) != 0)
    {
        printf("Failed. Error Code : %d", WSAGetLastError());
        return 1;
    }
     
    printf("Initialized.\n");
     
    //Create a socket
    if((socket1 = socket(AF_INET, SOCK_STREAM, 0 )) == INVALID_SOCKET)
    {
        printf("Could not create socket : %d" , WSAGetLastError());
		getch();
		return 0;
    }
	
    if((socket2 = socket(AF_INET, SOCK_STREAM, 0 )) == INVALID_SOCKET)
    {
        printf("Could not create socket 2: %d" , WSAGetLastError());
		getch();
		return 0;
    }
 
    printf("Sockets 1 and 2 created.\n");

	  //Prepare the sockaddr_in structure
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = INADDR_ANY;
    server.sin_port = htons( 8888 );
	    
    server2.sin_family = AF_INET;
    server2.sin_addr.s_addr = INADDR_ANY;
    server2.sin_port = htons( 8881 );
	
	
    //Bind
    if(bind(socket1, (struct sockaddr*)&server, sizeof(server)) == SOCKET_ERROR)
    {
        printf("Bind failed with error code : %d" , WSAGetLastError());
		getch();
		return 0;
    }
     
    if(bind(socket2, (struct sockaddr*)&server2, sizeof(server)) == SOCKET_ERROR)
    {
        printf("Bind failed with error code 2: %d" , WSAGetLastError());
		getch();
		return 0;
    }
	  
    puts("Bind done");

	//Listen to incoming connections
    listen(socket1, 3);
    listen(socket2, 3);

	/* Accepting and incoming connections */
    printf("Waiting for incoming connections 1 & 2...");
     
    c = sizeof(struct sockaddr_in);
    new_socket1 = accept(socket1, (struct sockaddr*)&client, &c);
    if (new_socket1 == INVALID_SOCKET)
    {
        printf("Accept failed with error code : %d", WSAGetLastError());
		getch();
		return 0;
    }     
    printf("\nConnection accepted 1");
	
		
    c2 = sizeof(struct sockaddr_in);
    new_socket2 = accept(socket2, (struct sockaddr*)&client2, &c2);
    if (new_socket2 == INVALID_SOCKET)
    {
        printf("Accept failed with error code 2: %d", WSAGetLastError());
		getch();
		return 0;
    }     
    printf("\nConnection accepted 2");

  
    closesocket(socket1);
    closesocket(socket2);

	getch();
    return 0;
}