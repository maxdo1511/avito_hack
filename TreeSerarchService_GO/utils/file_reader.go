package utils

import (
	"encoding/csv"
	"fmt"
	"os"
)

func ReadCSV(fileName string) [][]string {
	// Открываем CSV файл
	file, err := os.Open(fileName)
	if err != nil {
		fmt.Println("Error opening file:", err)
		return nil
	}
	defer file.Close()

	// Создаем новый ридер для файла CSV
	reader := csv.NewReader(file)

	// Читаем все записи из файла
	records, err := reader.ReadAll()
	if err != nil {
		fmt.Println("Error reading CSV:", err)
		return nil
	}

	return records
}

/*
 * Читает файл специфичный для Avito
 * @param fileName string путь к файлу
 * @return дерево конфигурации
 * @return имя конфигурации
 */
func ReadAvitoFile(fileName string) (string, *Node) {
	return ParseAvitoFile(fileName)
}
