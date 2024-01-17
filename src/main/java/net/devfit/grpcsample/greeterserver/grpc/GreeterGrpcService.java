package net.devfit.grpcsample.greeterserver.grpc;

import java.util.ArrayList;

import org.lognet.springboot.grpc.GRpcService;

import io.grpc.stub.StreamObserver;
import net.devfit.grpcsample.greeterproto.GreeterGrpc;
import net.devfit.grpcsample.greeterproto.GreeterReply;
import net.devfit.grpcsample.greeterproto.GreeterRequest;

@GRpcService
public class GreeterGrpcService extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHelloUnary(GreeterRequest request, StreamObserver<GreeterReply> responseObserver) {
        responseObserver.onNext(
                GreeterReply.newBuilder().setMessage("Hello," + request.getName()).build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void sayHelloServerStreaming(GreeterRequest request, StreamObserver<GreeterReply> responseObserver) {
        var greeterReply = GreeterReply.newBuilder().setMessage("Hello," + request.getName()).build();
        responseObserver.onNext(greeterReply);
        responseObserver.onNext(greeterReply);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<GreeterRequest> sayHelloClientStreaming(StreamObserver<GreeterReply> responseObserver) {
        var requests = new ArrayList<String>();
        return new StreamObserver<>() {
            @Override
            public void onNext(GreeterRequest greeterRequest) {
                requests.add(greeterRequest.getName());
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onCompleted() {
                var greeterReply = GreeterReply.newBuilder().setMessage("Hello, " + String.join(", ", requests)).build();
                responseObserver.onNext(greeterReply);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<GreeterRequest> sayHelloBidirectionalStreaming(StreamObserver<GreeterReply> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(GreeterRequest greeterRequest) {
                var greeterReply = GreeterReply.newBuilder().setMessage("Hello," + greeterRequest.getName()).build();
                responseObserver.onNext(greeterReply);
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

}
