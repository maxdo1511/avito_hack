package tree_search

import (
	"fmt"
	"go_tree_service/utils"
	"math/rand"
	"os"
	"path/filepath"
	"strconv"
	"strings"
	"time"
)

type LocationNode struct {
	ID          int64
	Name        string
	MaxBrunchID int64
	Costs       map[int64]float32
	Parent      *LocationNode
	Children    []*LocationNode
}

type CategoryNode struct {
	ID            int64
	Name          string
	MaxBrunchID   int64
	LocationCosts []int64
	Parent        *CategoryNode
	Children      []*CategoryNode
}

type TreeSearchServiceStruct struct {
	LocationTree *LocationNode
	CategoryTree *CategoryNode
}

var TreeSearchService *TreeSearchServiceStruct

func GetTree() *TreeSearchServiceStruct {
	return TreeSearchService
}

func InitTrees() {
	TreeSearchService = &TreeSearchServiceStruct{}
	baseMatrixData := utils.ReadCSV("data/baseMatrix.csv")
	var baseMatrix = make(map[int64]map[int64]float32)
	// Time test

	baseMatrixTimer := time.Now()

	// Формирование базовой матрицы
	for i := 0; i < len(baseMatrixData); i++ {
		categoryId, _ := strconv.Atoi(baseMatrixData[i][0])
		locationId, _ := strconv.Atoi(baseMatrixData[i][1])
		Price, _ := strconv.ParseFloat(baseMatrixData[i][2], 32)
		if baseMatrix[int64(categoryId)] == nil {
			baseMatrix[int64(categoryId)] = make(map[int64]float32)
		}
		baseMatrix[int64(categoryId)][int64(locationId)] = float32(Price)
	}
	// Time test

	durationBaseMatrix := time.Since(baseMatrixTimer)
	fmt.Println("BaseMatrix time:", durationBaseMatrix)

	// Формирование деревьев
	for _, path := range findAllConfigurations("data") {
		name, node := utils.ParseAvitoFile(path)
		switch name {
		case "location":
			{
				// Test time

				locationTimer := time.Now()

				// Формирование дерева
				GetTree().LocationTree = CreateLocationTree(node, baseMatrix)

				// Time test
				durationLocation := time.Since(locationTimer)
				fmt.Println("Location time:", durationLocation)
			}
		case "category":
			{
				// Test time
				categoryTimer := time.Now()

				// Формирование дерева
				GetTree().CategoryTree = CreateCategoryTree(node)

				// Time test
				durationCategory := time.Since(categoryTimer)
				fmt.Println("Category time:", durationCategory)
			}
		}
	}

	// Test time

	indexCategoryTreeCosts := time.Now()

	// Индексация цен в дереве
	IndexCategoryTreeCosts(GetTree().CategoryTree, baseMatrix)

	// Time test
	durationIndexCategoryTreeCosts := time.Since(indexCategoryTreeCosts)
	fmt.Println("IndexCategoryTreeCosts time:", durationIndexCategoryTreeCosts)

	// Test time

	addCategoryTimer := time.Now()

	// Добавляем тестовую локацию
	AddCategory(25, "Товары для детей и игрушки")

	// Time test
	durationAddCategory := time.Since(addCategoryTimer)
	fmt.Println("AddCategory time:", durationAddCategory)

	// Test time

	addLocationTimer := time.Now()

	// Добавляем тестовую локацию
	for j := 0; j < 100; j++ {
		AddLocation(int64(rand.Intn(4000-500+1)+500), "Какой-то город")
	}

	// Time test
	durationAddLocation := time.Since(addLocationTimer)
	fmt.Println("AddLocation time:", durationAddLocation)

	// Test time

	for j := 0; j < 30000; j++ {
		FindCostByLocationAndCategory(1, int64(rand.Intn(4000-1+1)+1), rand.Int63n(60-1+1)+1)
	}

	// Time test
	durationFindCostByLocationAndCategory := time.Since(indexCategoryTreeCosts)
	fmt.Println("FindCostByLocationAndCategory time:", durationFindCostByLocationAndCategory)
}

// Метод находит максимальноый id локации в которой установлена цена для данной категории
func IndexCategoryTreeCosts(root *CategoryNode, matrix map[int64]map[int64]float32) {
	if root == nil {
		return
	}
	if (root.Children == nil) || (len(root.Children) == 0) {
		IndexCategoryTreeCostsRecursive(root, matrix)
	}
	for i := 0; i < len(root.Children); i++ {
		IndexCategoryTreeCosts(root.Children[i], matrix)
	}
}

// openList - список всех предыдущих id, для которых пока не нашлось локаций
func IndexCategoryTreeCostsRecursive(currentNode *CategoryNode, matrix map[int64]map[int64]float32) {
	if currentNode.LocationCosts != nil {
		return
	}
	for locationID := range matrix {
		if matrix[locationID][currentNode.ID] > 0 {
			if currentNode.LocationCosts == nil {
				currentNode.LocationCosts = []int64{}
			}
			currentNode.LocationCosts = append(currentNode.LocationCosts, locationID)
		}
	}
	if currentNode.Parent == nil {
		return
	}
	IndexCategoryTreeCostsRecursive(currentNode.Parent, matrix)
}

func findAllConfigurations(dataDir string) []string {
	var paths []string

	root := dataDir

	err := filepath.Walk(root, func(path string, info os.FileInfo, err error) error {
		if err != nil {
			return err
		}
		if !info.IsDir() {
			if strings.HasSuffix(path, ".avito") {
				paths = append(paths, path)
			}
		}
		return nil
	})

	if err != nil {
		fmt.Println("Error walking the path:", err)
	}

	return paths
}

func FindCostByLocationAndCategory(userID int64, locationID int64, categoryID int64) float32 {
	if (categoryID == 0) || (locationID == 0) {
		return 0
	}
	if (categoryID > TreeSearchService.CategoryTree.MaxBrunchID) || locationID > TreeSearchService.LocationTree.MaxBrunchID {
		return 0
	}
	// Доходим по дереву локаций до нужного узла
	startLocationNode := FindLocationNodeByID(locationID)

	if startLocationNode == nil {
		return 0
	}

	// Проверяем есть ли цена на категорию
	if startLocationNode.Costs != nil {
		if startLocationNode.Costs[categoryID] != 0 {
			return startLocationNode.Costs[categoryID]
		}
	}
	// Не нашли => идём дальше

	// Доходим по дереву категорий до нужного узла
	startCategoryNode := FindCategoryNodeByID(categoryID)

	// Теперь идём вверх пока не будет найдена цена
	if startCategoryNode.Parent == nil || startCategoryNode == nil {
		return 0
	}

	return FindCostByLocationAndCategoryRecursive(startLocationNode, startCategoryNode, startCategoryNode)
}

func FindCostByLocationAndCategoryRecursive(locationNode *LocationNode, categoryNode *CategoryNode, startNode *CategoryNode) float32 {
	if locationNode.Costs[categoryNode.ID] != 0 {
		return locationNode.Costs[categoryNode.ID]
	}
	if utils.SearchInList(categoryNode.LocationCosts, locationNode.ID) {
		if locationNode.Parent == nil {
			return 0
		}
		return FindCostByLocationAndCategoryRecursive(locationNode.Parent, startNode, startNode)
	}
	if (categoryNode.Parent) == nil {
		if locationNode.Parent == nil {
			return 0
		}
		return FindCostByLocationAndCategoryRecursive(locationNode.Parent, startNode, startNode)
	}
	return FindCostByLocationAndCategoryRecursive(locationNode, categoryNode.Parent, startNode)
}

func printTreesRecursive() {
	PrintLocationTree(TreeSearchService.LocationTree, 0)
	PrintCategoryTree(TreeSearchService.CategoryTree, 0)
}
