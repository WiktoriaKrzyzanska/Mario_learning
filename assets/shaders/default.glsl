#type vertex
#version 330 core
layout (location=0) in vec3 aPos; // literka a - attribute
layout (location=1) in vec4 aColor; //kolor

out vec4 fColor; //f to fragment

void main(){
    fColor = aColor;
    gl_Position = vec4(aPos, 1.0);
}

#type fragment
#version 330 core

in vec4 fColor; //mamy wyżej out więc musimy mieć in, żeby nie było błędu

out vec4 color; //mówimy jaki kolorek

void main(){   //mamy always main
    color = fColor; // colors slowly changes also known as gradient :)
}