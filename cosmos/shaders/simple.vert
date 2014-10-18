#version 130

in vec3 position;
in vec3 color;

smooth out vec4 fragColor;

uniform mat4 geoTransform;
uniform mat4 perspectiveMatrix;

void main() {
    vec4 modelPos = vec4(position.x, position.y, position.z, 1);

    gl_Position = perspectiveMatrix * geoTransform * modelPos;

    fragColor = vec4(color.x, color.y, color.z, 1);
}