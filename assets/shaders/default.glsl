#type vertex
#version 330 core
layout (location=0) in vec3 aPos; // literka a - attribute
layout (location=1) in vec4 aColor; //kolor


uniform mat4 uProjection;
uniform mat4 uView;


out vec4 fColor; //f to fragment

void main(){
    fColor = aColor;
    gl_Position =uProjection * uView* vec4(aPos, 1.0);
}

#type fragment
#version 330 core

uniform float uTime;

in vec4 fColor; //mamy wyżej out więc musimy mieć in, żeby nie było błędu

out vec4 color; //mówimy jaki kolorek

void main(){   //mamy always main
//    color = sin(uTime) * fColor; // colors slowly changes also known as gradient :)
//    //sin makes the cube blink
//    float avg = (fColor.r + fColor.g + fColor.b ) / 3; // odcienie czerni to suma wszystkich kolorow podzielona przez ich ilosc
//    color = vec4(avg, avg, avg, 1);
    float noise = fract(sin(dot(fColor.xy, vec2(12.9898,78.233))) * 43758.5453);
    color = fColor * noise;
}