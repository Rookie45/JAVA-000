package io.github.kimmking.gateway.inbound;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HttpInboundInitializer extends ChannelInitializer<SocketChannel> {
	
	private String proxyServer;

	private List<String> proxyServers;

	public HttpInboundInitializer(String proxyServer) {
		this.proxyServer = proxyServer;
		this.proxyServers = Arrays.asList(proxyServer);
	}

	public HttpInboundInitializer(List<String> proxyServers) {
		this.proxyServer = proxyServers.get(0);
		this.proxyServers = proxyServers;
	}

	@Override
	public void initChannel(SocketChannel ch) {
		ChannelPipeline p = ch.pipeline();
//		if (sslCtx != null) {
//			p.addLast(sslCtx.newHandler(ch.alloc()));
//		}
		p.addLast(new HttpServerCodec());
		//p.addLast(new HttpServerExpectContinueHandler());
		p.addLast(new HttpObjectAggregator(1024 * 1024));
//		p.addLast(new HttpInboundHandler(this.proxyServer));
		p.addLast(new HttpInboundHandler(this.proxyServers));
	}
}
