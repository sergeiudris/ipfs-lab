
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
- most important: processes and queues
  - programs and files should be written compeltely differently: functions shouldn't mutate things, but return new maps or channels or launch goroutines and put on channels
  - it's not evena language thing, but decoupling stuff and going away from function chains to an opetaion that has many steps, each calls a func which does not mutate args but returns new or launches goroutine
  - while we go through this op we wait for channels to return and in the end response
  - most of code (like 70-80% is trying to do flow control and fixes and patches of that attmept)
  - should be async api (http and programmtic) -> operation -> it calls smaller fucntions from libarires(namespaces) that all packages share -> change state right here, in op fn, explictely -> response
  - a package is always a lib: it either provides some standard just-functions-api, or it gives api to create state and then start a process and give it state; so the the main.go of the program should have all the processes laucnhed and the states, and then via operations we talk to the program and change state flatly within those ops, no nesting of procA laucnhes procB, procB -> procC...; instead, all launched in the main and pass channels to them as arguments, and now they talk over channels, anware (which is awesome) on who's on the other end of th channel
- separation of concerns
  - decouple things
- defer is bad
- multiple returns is bad 

#### ~~PR with test into seed~~

- <s>PR with test on current issue
- find the problem and create the seed for solution (repo + lib repos); repos should be named as they would be in /ipfs, but prefixed with ipfs (so username/ipfs-actualnameofthesystemrepo  username/ipfs-actualnameofthelibsrepo)</s>

#### docker sdk

- https://docs.docker.com/engine/api/sdk/
- https://github.com/docker/go-docker

#### contirbuting to other peoples projects(repos) is a continuous thing (with heart and effort, digging into how their creation works) 

- it's not "make N contirbutions" and graduate to your own thing
- it's both create AND keep on contributing (meaningfully, no surface, but hardest issues, deep diving), spending time and effort making creation of thers work
- it's the same as playing existing games or visiting friends or exploring - it's not about you, but "hey, they are making these, i'll solve these issues, be sweaty and angry, but contirbute"
- and it's a non-exclusion thing, connecting and mutually accepting; plus, it's win win benfitail - keeps us down, humble; and it's like a new game/puzzle/movie - see how new mechanism work, keeps mind non-stale and open