package main

import (
	"fmt"
	"math"
)

func main() {
	// fmt.Println(math.pi) 
	// ->  cannot refer to unexported name math.pi
	fmt.Println(math.Pi)
}
