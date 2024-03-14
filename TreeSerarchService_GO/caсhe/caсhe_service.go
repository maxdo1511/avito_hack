package caсhe

import (
	"github.com/bluele/gcache"
	"time"
)

type CacheNode struct {
	Value string
	Count int8
}

var SecondLevelCache = make(map[string]CacheNode)
var FirstLevelCache gcache.Cache
var expireTime = int64(0)

func initCache(cacheSize int, et int64) {
	FirstLevelCache = gcache.New(cacheSize).LRU().Build()
	expireTime = et
}

// метод добавляет в кеш второго уровня, если элемент уже есть то увеличивает счетчик
// если больше 10 то удаляет из кеша и добавляет второй уровень
func AddToSecondLevelCache(key string, value string) {
	valueNode := CacheNode{
		Value: value,
		Count: 1,
	}
	SecondLevelCache[key] = valueNode
	if len(SecondLevelCache) > 10 {
		delete(SecondLevelCache, key)
	}
}

// метод проверяет наличие в кеше и возвращает его, если нет то возвращает nil
func GetFromSecondLevelCache(key string) *CacheNode {
	value, found := SecondLevelCache[key]
	if !found {
		return &CacheNode{}
	}
	value.Count = value.Count + 1
	return &value
}

func AddToFirstLevelCache(key string, value CacheNode) {
	err := FirstLevelCache.SetWithExpire(key, value, time.Duration(expireTime))
	if err != nil {
		panic(err)
	}
}

func GetFromFirstLevelCache(key string) CacheNode {
	value, err := FirstLevelCache.Get(key)
	if err != nil {
		panic(err)
	}
	return value.(CacheNode)
}
