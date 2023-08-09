package main

import (
	"fmt"
	"sync"
	"time"
)

func main() {
	waitForDone := &sync.WaitGroup{}
	startConsumer(waitForDone)

	fmt.Println("run server")

	waitForDone.Wait()
	fmt.Println("graceful shutdown")
}
func startConsumer(waitForDone *sync.WaitGroup) {
	waitForDone.Add(1)
	go func() {
		defer waitForDone.Done()

		for i := 0; i < 10; i++ {
			pollForMessage()
			time.Sleep(1 * time.Second)
		}
	}()
}

func pollForMessage() {
	defer func() {
		if r := recover(); r != nil {
			fmt.Println("Recovered poll for message from SQS", r)
		}
	}()

	fmt.Println("polling for message from SQS")
	// validate & process message
	// delete message
}
