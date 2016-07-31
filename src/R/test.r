library(e1071)
data=read.matrix.csr("Q:\\Documents\\Bitirme\\Feature Vector\\feature_vector_trimmed_libsvm.txt")
set.seed(12345)



setClass("empty.is.0")
setAs("character", "empty.is.0", 
      function(from) replace(as.numeric(from), from == "", 0))
test = read.csv("Q:\\Documents\\Bitirme\\Feature Vector\\feature_vector_r.txt", as.is=TRUE, header=FALSE,strip.white = TRUE, colClasses = "empty.is.0")


test = read.csv("C:\\Users\\Ozan\\Desktop\\FeatureVector\\TANInew\\feature_vector.txt", nrows=1, skip=1, as.is=TRUE)[,-(22002:47169)][,-1]
rfTest = randomForest(V1 ~ ., test, ntree=3, norm.votes=FALSE)

library("foreach")
library(randomForest)
set.seed(12345)
test = read.csv("Q:\\Documents\\Bitirme\\Feature Vector\\feature_vector_r.txt", as.is=TRUE, header=FALSE)
rfTest = randomForest(V1 ~ ., test, ntree=1, norm.votes=FALSE, replace=FALSE, nodesize=150, maxnodes=20, mtry=30)



library("foreach")
library("doSNOW")
registerDoSNOW(makeCluster(4, type="SOCK"))
rfTest <- foreach(ntree = rep(250, 4), .combine = combine, .packages = "randomForest") %dopar% rfTest = randomForest(V1 ~ ., test, ntree=1, norm.votes=FALSE, replace=FALSE, nodesize=150, maxnodes=20, mtry=30)