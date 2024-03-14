package utils

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

type Node struct {
	Name     string
	Children []*Node
	Parent   *Node
}

func ParseAvitoFile(fileName string) (string, *Node) {
	var sectionName = "default"

	file, err := os.Open(fileName)
	if err != nil {
		fmt.Println("Error opening file:", err)
		return sectionName, nil
	}
	defer file.Close()

	root := &Node{Name: "Root"}
	currentNode := root
	indentLevel := 0

	// -1 - имя конфигурации
	// 0 - вложенная конфигурация
	var state = -1

	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		line := scanner.Text()

		// Пропускаем пустые строки и комментарии
		if line == "" || line[0] == '#' {
			continue
		}

		// Ищем конфигурацию и запоминаем имя
		if line[0] == '$' && state == -1 {
			sectionName = strings.TrimSpace(line[1:])
			state = 0
			continue
		}

		if state == -1 {
			continue
		}

		if state != 0 {
			continue
		}

		// Определяем уровень вложенности по количеству табуляций
		level := countTabs(line)

		name := strings.TrimSpace(line)

		// Если уровень вложенности увеличился, добавляем новый узел
		if level > indentLevel {
			currentNode = currentNode.Children[len(currentNode.Children)-1]
		} else if level < indentLevel {
			// Если уровень вложенности уменьшился, поднимаемся на нужный уровень выше
			for i := 0; i < indentLevel-level; i++ {
				currentNode = currentNode.Parent
			}
		}

		newNode := &Node{Name: name, Parent: currentNode}
		currentNode.Children = append(currentNode.Children, newNode)
		indentLevel = level
	}

	if err := scanner.Err(); err != nil {
		fmt.Println("Error reading file:", err)
	}

	return sectionName, root
}

func countTabs(line string) int {
	tabCount := 0
	tabSize := 4
	for i := 0; i < len(line); i += tabSize {
		if (i + tabSize) > len(line) {
			break
		}
		if line[i:i+tabSize] == "    " {
			tabCount++
		} else {
			break
		}
	}
	return tabCount
}

func PrintTree(node *Node, level int) {
	for _, child := range node.Children {
		for i := 0; i < level; i++ {
			fmt.Print("    ")
		}
		fmt.Println(child.Name)
		PrintTree(child, level+1)
	}
}
