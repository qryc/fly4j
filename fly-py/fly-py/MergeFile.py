import os.path
import shutil

outputDir = "/Users/gw/QRYC/KnPdf/"
outputPicDir = "/Users/gw/QRYC/KnPdf/pic/"
if(os.path.exists(outputDir)):
    shutil.rmtree(outputDir)
def addFoldersWithChildren2String(floderDir,str):
    for file in os.listdir(floderDir):
        file_path = os.path.join(floderDir, file)
        if os.path.isdir(file_path):
            print("文件夹：", file_path)
            addFoldersWithChildren2String(file_path,str)
        else:
            print("文件：", file_path)

floderDir = "/Volumes/HomeWork/2-article/DocPublic/0 我的知识库PDF-"
str=""
addFoldersWithChildren2String(floderDir,str)