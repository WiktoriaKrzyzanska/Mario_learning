#type vertex
#version 330 core
layout (location=0) in vec3 aPos; // literka a - attribute
layout (location=1) in vec4 aColor; //kolor
layout (location=2) in vec2 aTextCoords;


uniform mat4 uProjection;
uniform mat4 uView;


out vec4 fColor; //f to fragment
out vec2 fTextCoords;

void main(){
    fColor = aColor;
    fTextCoords = aTextCoords;
    gl_Position =uProjection * uView* vec4(aPos, 1.0);
}

#type fragment
#version 330 core

uniform float uTime;
uniform sampler2D TEX_SAMPLER;

in vec4 fColor; //mamy wyżej out więc musimy mieć in, żeby nie było błędu
in vec2 fTextCoords;

out vec4 color; //mówimy jaki kolorek

void main(){   //mamy always main
//    color = sin(uTime) * fColor; // colors slowly changes also known as gradient :)
//    //sin makes the cube blink
//    float avg = (fColor.r + fColor.g + fColor.b ) / 3; // odcienie czerni to suma wszystkich kolorow podzielona przez ich ilosc
//    color = vec4(avg, avg, avg, 1);
//    float noise = fract(sin(dot(fColor.xy, vec2(12.9898,78.233))) * 43758.5453);
//    color = fColor * noise; // it creates noise effect
//    color = fColor;
    color = texture(TEX_SAMPLER, fTextCoords);
}