package foo

import (
	"fmt"
	"testing"
)

func TestFoo(t *testing.T) {

	fmt.Println("bar")

}

func TestAccessingPrivateFieldInStruct(t *testing.T) {

	// https://stackoverflow.com/questions/42664837/how-to-access-unexported-struct-fields/43918797#43918797

	// api0Refelction := reflect.ValueOf(api0).Elem()
	// configReflection := api0Refelction.FieldByName("config")
	// configReflection = reflect.NewAt(configReflection.Type(), unsafe.Pointer(configReflection.UnsafeAddr())).Elem()
	// fmt.Println(configReflection.Interface())
	// prints config field (struct) value

}
