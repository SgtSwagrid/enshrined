#version 400 core

in vec3 vertex;
in vec2 texmap;
in vec3 normal;

out vec2 texmap_;
out vec3 normal_;
out vec3 position_;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 transform;

void main(void) {
	
	gl_Position = projection * view * transform * vec4(vertex, 1.0);
	texmap_ = texmap;
	normal_ = (transform * vec4(normal, 0.0)).xyz;
	position_ = (transform * vec4(vertex, 1.0)).xyz;
}