#version 130

in vec4 position;
in vec4 color;

smooth out vec4 theColor;

uniform vec2 offset;
uniform float zNear;
uniform float zFar;
uniform float frustumScale;

void main() {
    vec4 cameraPos = position + vec4(offset.x, offset.y, 0.0f, 0.0f);

    vec4 clipPos;
    clipPos.xy = cameraPos.xy * frustumScale;
    clipPos.z = cameraPos.z*(zNear + zFar) + 2*zNear*zFar;
    clipPos.z /= zNear - zFar;
    clipPos.w = -cameraPos.z;

    theColor = color;

    gl_Position = clipPos;
}