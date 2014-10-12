#version 130

in vec4 position;
in vec4 color;

smooth out vec4 theColor;

uniform vec2 offset;
uniform mat4 perspectiveMatrix;
uniform mat4 rotationMatrix;

void main() {
    vec4 cameraPos = position + vec4(offset.x, offset.y, 0.0f, 0.0f);

    gl_Position = perspectiveMatrix * rotationMatrix * cameraPos;

    theColor = color;
}