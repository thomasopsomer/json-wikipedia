default:

test: 
	mvn test

build: 
	mvn clean package

clean:
	mvn clean


# phonies

.PHONY: default clean test build