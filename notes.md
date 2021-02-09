
#### gloang reflection: accessing private field in struct

```golang

// https://stackoverflow.com/questions/42664837/how-to-access-unexported-struct-fields/43918797#43918797

api0Refelction := reflect.ValueOf(api0).Elem()
configReflection := api0Refelction.FieldByName("config")
configReflection = reflect.NewAt(configReflection.Type(), unsafe.Pointer(configReflection.UnsafeAddr())).Elem()
fmt.Println(configReflection.Interface())
// prints config field (struct) value



```

#### golang: structs, maps, polymorphism and namespaces

- golang is object oriented, no secret here
- why structs are used everywhre and not map? polymorhism - because only structs allow adding funcs on them
- golang's namespace of a service is essentially a process and it's opertaions, and main struct defined the state of the process
- but: structs everywhere with methods on them is incorrect, boject oriented and makes it difficult to reason about the program
- and function chains is also a mistake - intead should be an operation (over rest or api call) that's one function calling other in steps
- for that  - processes and queues (go blocks, processes and channels) are needed; but it's more about seprating concerns and decoupling things, rather than language
- now, what can be done better even within low level nature (execpt for channels, processes and go blocks which are amazing) of go
  - no nesting: packages should be all flatly located in one directory, same as e.g. `github/ipfs` - REPOS ARE NOT NESTED! and it's AMAZING, same should be with module - all packages in one dir with names like `foo` `foo-bar` `foo-bar-baz`, one level
  - namespaces: golang as javascript has a catastrophic flow of not having namesapces; it's CHAOS - sometimes pacakge is `foo` but file is `fooapi`, it RANDOM; it should be `one file for one package`; yes, you heard correctly - one file one package, it's a namespace, which means every file should be in it's own dir... bad, but otherwise insane
  - maps + interfaces: avoid structs, use maps and interfaces, 
  - minimum methods: polymorphism only when really need to act base on type,namespacing instead for polymorphism, don't put ssame name fn in one  file - instead, use namespacing `foo.go/dosmth` `bar.go/dosmth`, all fns take args, no methods
    - but no: we cannot do polymorphysm without structs at all... ok, to minimum
  - that's it structure-wise: no nesting, namespaces, very very few structs, maps and interfaces (to be able to jump around with intellisense)
  - basically, typescript but with channels and go blocks