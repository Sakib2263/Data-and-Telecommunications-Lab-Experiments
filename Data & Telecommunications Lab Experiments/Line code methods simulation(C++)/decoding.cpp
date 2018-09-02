#include<iostream>
#include<string>
using namespace std;

int main(void)
    {
    string data1,data2,data3;

    // Lets first print the clock signal
    //for(int i=0;i<len;i++)  cout<<"";

    // NRZ Algorithm
    // In NRZ algorithm we represent each 0 by __ and each 1 by --
    cout<<"Enter the data for NRZ-L:-  ";
    cin>>data1;
    int len = data1.length();
    cout<<endl<<endl<<"NRZ-L decoding"<<endl<<endl;
    for(int i=0;i<len;i++){
        if( data1[i] == '-' ) cout<<"0";
        else if ( data1[i] == '_' )  cout <<"1";
        }



    cout<<endl<<endl<<"NRZI decoding"<<endl<<endl;
    cout<<"Enter the data for NRZ-I:-  ";
    cin>>data2;
    int len2 = data2.length();
    //cout<<len2;
    bool state = true;
    string nrzi="";
    for(int i=0; i<len2 ; i++)
        {
        if( i==0 ){
            if( data2[i] == '-') {
                    nrzi+= "0";
            }
            else {
                    nrzi+="1";
            }

        }
        else if ( data2[i] == '-' ) {
            if( data2[i-1] == '-' && state==true) nrzi += "0";
            else {nrzi += "1";
                if(state==true) state= false;
                 else state = true;
            }

        }
        else if( data2[i] == '_' ) {
            if( data2[i-1] == '_'&& state==true) nrzi += "0";
            else {
                    nrzi += "1";
                    if(state==true) state=false;
                    else state = true;

            }

        }
        }
        cout<<nrzi;

/*

   cout<<endl<<endl<<"RZ decoding"<<endl<<endl;
     //Rz Encoding
        for(int i=0;i<len;i++){
        if( data[i] == '0' ) cout<<"__..";
        else    cout <<"--..";
        }
*/

    }
