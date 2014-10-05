#version 130

out vec4 outputColor;

uniform float colorPeriod;
uniform float time;

const vec4 firstColor = vec4(0.0f, 1.0f, 1.0f, 1.0f);
const vec4 secondColor = vec4(0.0f, 0.0f, 1.0f, 1.0f);

void main() {
    float frequency = 3.14159f * 2.0f / colorPeriod;
    float currLerp = 0.5f * sin(frequency * time) + 0.5;

    outputColor = mix(firstColor, secondColor, currLerp);
}