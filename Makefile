
all:
	android update project --path `pwd` --target 3 -n EHealth
	ant compile

clean:
	rm -rf bin build.xml default.properties local.properties gen
