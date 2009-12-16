
all: 
	android update project --path `pwd` --target 3 -n EHealth
	ant #debug
