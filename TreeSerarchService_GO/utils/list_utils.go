package utils

func SearchInList(list []int64, value int64) bool {
	for _, v := range list {
		if v == value {
			return true
		}
	}
	return false
}
