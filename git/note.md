# 一、创建版本库与简单的提交修改

   1. git init--初始仓库
 
   2. git add file1 file2--添加文件，等待提交
  
   3. git commit -m ""--提交文件，-m参数后面加提交的说明信息
  
   4. git status--查看仓库当前状态，是否有文件被修改，是否有文件已经add等待提交
   
   5. git diff file--查看指定文件修改的内容
      
      文件修改之后用这个命令能看到修改的内容，但是把文件add了就看不到修改的内容了
      
      如果git status告诉你有文件被修改过，用git diff可以查看修改内容

# 二、版本控制

   1. git log--显示从最近到最远的提交日志
   
      1. commit c15c09dafe1a455acf6265a8a5005acc222a8852 这种数字是commit的id
      
   2. git reset--版本回退(代码回滚)    
        
      HEAD表示当前版本，HEAD^表示上个版本，HEAD^^表示上上个版本，上100个版本用HEAD~100表示
       
      1. 回退到之前某个版本--git reset --hard HEAD^
      
      2. 回退到之前某个版本之后要去未来某个版本--git reset --hard 未来版本的commit id前几位
      
         未来版本的commit id前几位可以往上滚动命令去查找
      
         git reset --hard 08fb06120fa
         
      3. 如果往上滚动，或者关机了重启电脑无法找到commit id的前几位，可以用--git reflog查看HEAD指针指向commit id的历史
      
      4. git log 和 reflog的区别：
          
         git log可以显示所有提交过的版本信息，不包括已经被删除的 commit 记录和 reset 的操作

         git reflog是显示所有的操作记录，包括提交，回退的操作。一般用来找出操作记录中的版本号，进行回退。

         git reflog常用于恢复本地的错误操作。

            场景：我们commit了一个操作，发现提交的是错误的，我们进行了回退，git reset HEAD^,也进行了checkout 操作，就是把工作区的文件也回退还原了，                 这时候发现commit的没有问题。等于说删了不该删的了，咋整，想再回退到删除之前的。找到之前的版本号进行回退，使用git log发现那个提交的版本号记录根本不存在了，这时候就可以用reflog了。
            
   3. 工作区和暂存区
      
      1. 工作区：仓库目录就是工作区
      
      2. 版本库：.git目录
         
      3. 暂存区：.git目录里面的index（或者是stage）
         
      4. 工作过程：把文件往Git版本库里添加的时候，是分两步执行的：第一步是用git add把文件添加进去，实际上就是把文件修改添加到暂存区；第二步是用git commit提交更改，实际上就是把暂存区的所有内容提交到当前分支。
      
   4. 管理修改：第一次修改文件后add，不commit，紧接着第二次修改文件，不add，直接commit，这时提交到分支的内容只是第一次add后的内容，第二次修改的内容不会被提交，因为第二次修改后的内容还没有add存到暂存区，不会加入到commit中，这时需要再add一次，把第二次修改添加到暂存区，再commit就行了。或者在第二次修改后紧接着第二次add，最后直接commit，等于说把两次修改合并后一块提交了
      
      可以用git diff HEAD -- readme.txt查看工作区和版本库中最新版本的区别
      
   5. 撤销修改
   
      1. 撤销已编辑的修改--git checkout -- readme.txt
      
         一种是readme.txt自修改后还没有被放到暂存区，现在，撤销修改就回到和版本库一模一样的状态
         
         一种是readme.txt已经添加到暂存区后，又作了修改，现在，撤销修改就回到添加到暂存区后的状态
         
         总之，就是让这个文件回到最近一次git commit或git add时的状态
      
      2. 撤销已经add到暂存区的内容，重新放回工作区--git reset HEAD readme.txt HEAD表示最新的版本
      
      3. 已经commit到版本库，想要撤销，用版本回退命令--git reset commit id
      
      4. 场景：
        
         在工作区修改a.txt文件，还没add，想撤销修改，用git checkout -- a.txt，回到和版本库一模一样的状态（回到最近一次git commit的状态）
         
         在工作区修改a.txt文件，并且add到暂存区，还没commit，再次修改a.txt文件，这时想撤回到第一次修改后的状态，用git checkout -- a.txt，回到添加到暂存区后的状态（回到最近一次git add的状态）
         
         修改完a.txt文件，add到暂存区，想要撤回暂存区的内容，重新放回工作区修改，用git reset HEAD readme.txt
         
   6. 删除文件
   
      1. 在工作区手动把文件删了，然后用git add a.txt或者git rm a.txt，暂时理解为保存到暂存区，然后git commit提交到版本库，把版本库对应文件删了
      
      2. 或者直接在工作区执行命令git rm a.txt，然后直接git commit也行
      
      3. 如果手动删除文件，但是删错了想恢复，在还未git commit的情况下，可以用git checkout -- a.txt把版本库对应的最新的文件恢复回来，其实是用版本库里的版本替换工作区的版本，无论工作区是修改还是删除，都可以“一键还原”。checkout的用法再看看撤销修改的联系一下
      
      4. 注意：从来没有被添加到版本库就被删除的文件，是无法恢复的！
      
# 三、分支管理

   1. 前面版本的分支只要没新提交，合并后面版本的分支肯定不会冲突如test分支新建test1分支，test1有新提交，test没有，那test合并test1不会冲突如果test1和test都有新提交，那合并就可能会冲突
         
# 四、远程仓库

   可以在github远程建库然后在本地克隆，也可以在本地建库然后关联github的仓库然后推送本地的内容上去，前提都要在github添加SSH key

   1. 在使用远程仓库之前，首先要在github添加SSH key

      1. 创建SSH key--ssh-keygen -t rsa -C "youremail@example.com"

      2. 创建完之后会在用户主目录创建.ssh目录，里面有id_rsa(私钥)和id_rsa.pub(公钥)两个文件

      3. 在github的settings中添加一个SSH key，title随便写，Key文本框里粘贴id_rsa.pub文件的内容

      4. 使用SSH Key的原因：

         1. 因为GitHub需要识别出你推送的提交确实是你推送的，而不是别人冒充的，而Git支持SSH协议，所以，GitHub只要知道了你的公钥，就可以确认只有你自己才能推送。公钥是给别人看的，私钥要自己保存好。信息用公钥加密，你自己用对应的私钥才能解密

         2. GitHub允许你添加多个Key。假定你有若干电脑，你一会儿在公司提交，一会儿在家里提交，只要把每台电脑的Key都添加到GitHub，就可以在每台电脑上往GitHub推送了。

   2. 本地库关联远程仓库：

      1. 本地建个库，add和commit需要的文件，

      2. 关联远程仓库--git remote add origin git@github.com:gunnersK/yzl.git

         如果关联报错，试试以下方法：git remote rm origin，之后重新关联--git remote add origin git@github.com:gunnersK/yzl.git

      3. 推送到远程仓库--git push -u origin master

          注意：第一次关联改仓库的时候不要直接打git push，会报错。由于远程库是空的，我们第一次推送master分支时，加上了-u参数，Git不但会把本地的master分支内容推送的远程新的master分支，还会把本地的master分支和远程的master分支关联起来，在以后的推送或者拉取时就可以简化命令(直接git push)

      4. 如果要推送已有的项目，由于文件较多，不可能一个个add，可以直接add文件夹

   3. 克隆远程仓库到本地：

      1. git clone git@github.com:gunnersK/yzl.git
      
      2. 拉下来之后只有master分支，如果需要拉其他分支，先在本地创建并切换到一个同名分支，然后再pull远程的分支下来

# 五、多人协作（拉取推送）

   1. push

      1. git push origin --delete <远程分支名字> 删除远程分支

      2. git branch -d dev  删除本地分支（不能删除当前的分支）

      3. git push origin dev

      4. git push 是推送当前分支到服务器，push之前最好先检查自己所在的分支

   2. pull

      1. 指定本地dev分支与远程origin/dev分支的链接
          git branch --set-upstream-to=origin/dev dev

      2. git pull <远程主机名> <远程分支名>:<本地分支名>
      
         1. 如果从远程pull本地没有的新分支，则会自动合并当前分支！所以最好先在本地建分支并切到该分支，再pull
      
   3. 如果同事先往github推送了dev，然后我再推送dev，如果推送不上去，则把pull下来合并本地dev，如果pull的时候被reject，则把本地dev先建一个分支再删除dev
      然后再把github上面的dev拉下来，再merge刚创建的分支，然后再把dev推送到github上去
      
# 六、忽略特殊文件      

   1. 在Git工作区的根目录下创建一个特殊的.gitignore文件，然后把要忽略的文件名填进去，Git就会自动忽略这些文件，配置文件可以参考https://github.com/github/gitignore
   
   2. .gitignore写法
   
      *.sample 　　 # 忽略所有 .sample 结尾的文件
      
      !lib.sample 　　 # 但 lib.sample 除外
      
      /TODO 　　 # 仅仅忽略项目根目录下的 TODO 文件，不包括 subdir/TODO
      
      build/ 　　 # 忽略 build/ 目录下的所有文件
      
      doc/*.txt 　　# 会忽略 doc/notes.txt 但不包括 doc/server/arch.txt
   
   
   3. .gitignore文件规则不生效解决方法

      把某些目录或文件加入忽略规则，按照上述方法定义后发现并未生效，原因是.gitignore只能忽略那些原来没有被追踪的文件，如果某些文件已经被纳入了版本管理中，则修改.gitignore是无效的。那么解决方法就是先把本地缓存删除（改变成未被追踪状态），然后再提交：
      
      git rm -r --cached .
      
      git add .
      
      git commit -m 'update .gitignore'
      
      

   
   
         
      
         
