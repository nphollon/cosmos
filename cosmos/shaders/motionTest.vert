#version 130

in vec4 position;

uniform float period;
uniform float time;

void main() {
    float frequency = 3.14159f * 2.0f / period;
    float xOffset = 0.5f * cos(frequency * time);
    float yOffset = 0.5f * sin(frequency * time);
    gl_Position = position + vec4(xOffset, yOffset, 0, 0);
}