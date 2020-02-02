package com.xwintop.xTransfer.receiver.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ReceiverConfigFtp
 * @Description: Ftp接收器配置
 * @author: xufeng
 * @date: 2018/4/9 11:25  
 */

@Data
public class ReceiverConfigFtp implements ReceiverConfig {
    private String serviceName = "receiverFtp";//对应服务名称
    private String id;//如果留空则系统自动分配
    private boolean enable = true;//是否开启
    private boolean async = false;//是否异步执行
    private boolean exceptionExit = false;//是否发生异常时退出任务

    private String host;                //FTP主机地址
    private int port = 21;              //FTP主机端口
    private String user;                //FTP用户名
    private String password;            //FTP密码
    private int connectionType = 0; //连接类型（0:FTP 1:FTP using implicit SSL 2:FTP using explicit SSL(Auth SSL)  3:FTP using explicit SSL(Auth TLS)）
    private int timeout = 300000;       //Ftp服务器超时限制，单位为毫秒
    private int socketTimeout = 300000; //Socket连接超时限制，单位为毫秒；用于FTP服务器未知的情况，避免接收器长时间无响应；非特别情况下无需指定
    private boolean longConnection;     //连接方式：长连接或每次接收消息时创建连接；使用长连接可以提高FTP接收器的接收消息的速度
    private int connectionPoolSize = 10;//ftp连接池大小
    private boolean passive = false;    //连接模式：主动式和被动式；缺省为主动式
    private boolean binary = false;     //消息传输方式：ASCII码和二进制；默认为ASCII码方式
    private Integer fileType; //文件传输方式（设置除二进制、ACSII方式之外的传输方式）
    private Integer bufferSize; //缓冲数据流缓冲区大小
    private String tmpPath;             //远程临时路径。用于存放中间读入的消息文件。读取完成后（消息成功放入队列）将删除该文件。同一FTPServer的多个Remote Path共享一个TMP路径
    private boolean hasTmpPath = true;  //是否使用临时目录，该选项与Tmp Path和Postfix参数相关
    private String serversEncoding = "AUTO";//编码格式
    private String prot = "P";//数据通道保护等级（ftps独有）
    private boolean checkServerValidity = false;//是否检测服务器证书有效性（ftps独有）

    private List<ReceiverConfigFtpRow> receiverConfigFsList = new ArrayList<>();

    @Data
    public static class ReceiverConfigFtpRow implements Serializable{
        private String remotePath;              //源文件路径，必填。最终的文件扫描路径
        private int max = 100;                  //一次读取扫描夹中的文件的最大数量
        private long maxSize = 4194304;         //处理文件最大长度
        private String encoding = "AUTO";       //文件编码格式
        private boolean delReceiveFile = true;  //是否删除原文件
        private String fileNameRegex;           //文件名正则表达式过滤；
        private boolean includeSubdirectory;    //是否接收该目录的子目录。
        private long delayTime = 0;             //延时过滤，单位为毫秒。即文件最后修改时间与当前时间之差小于设置值时不处理该文件，默认为0
        private long minSize = -1;              //文件最小长度过滤，默认为-1，否则当文件长度小于该值时不处理该文件
        private String bigFilePath;             //大文件保存目录
    }
}
