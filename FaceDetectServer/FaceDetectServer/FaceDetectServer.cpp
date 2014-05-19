#include<io.h>
#include<stdio.h>
#include<winsock2.h>
#include<conio.h>
 
#pragma comment(lib,"ws2_32.lib") //Winsock Library
 
int main(int argc , char *argv[])
{
    WSADATA wsa;
    SOCKET socket1, socket2;
    printf("\nInitializing Winsock...");
    if (WSAStartup(MAKEWORD(2, 2), &wsa) != 0)
    {
        printf("Failed. Error Code : %d", WSAGetLastError());
        return 1;
    }
     
    printf("Initialised.\n");
     
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
     
  
    closesocket(socket1);
    closesocket(socket2);

	getch();
    return 0;
}