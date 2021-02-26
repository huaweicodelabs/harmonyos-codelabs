package manager;

import ohos.rpc.*;

public abstract class NewsDemoIDLStub extends RemoteObject implements INewsDemoIDL {
    private static final String DESCRIPTOR = "com.huawei.codelab.INewsDemoIDL";

    private static final int COMMAND_TRAN_SHARE = IRemoteObject.MIN_TRANSACTION_ID;

    public NewsDemoIDLStub(/* [in] */ String descriptor) {
        super(descriptor);
    }

    @Override
    public IRemoteObject asObject() {
        return this;
    }

    public static INewsDemoIDL asInterface(IRemoteObject object) {
        if (object == null) {
            return null;
        }

        INewsDemoIDL result = null;
        IRemoteBroker broker = object.queryLocalInterface(DESCRIPTOR);
        if (broker != null) {
            if (broker instanceof INewsDemoIDL) {
                result = (INewsDemoIDL) broker;
            }
        } else {
            result = new NewsDemoIDLProxy(object);
        }

        return result;
    }

    @Override
    public boolean onRemoteRequest(
            /* [in] */ int code,
            /* [in] */ MessageParcel data,
            /* [out] */ MessageParcel reply,
            /* [in] */ MessageOption option)
            throws RemoteException {
        String token = data.readInterfaceToken();
        if (!DESCRIPTOR.equals(token)) {
            return false;
        }
        if (code == COMMAND_TRAN_SHARE) {
            String title = data.readString();
            String reads = data.readString();
            String likes = data.readString();
            String content = data.readString();
            String image = data.readString();
            tranShare(title, reads, likes, content, image);
            reply.writeNoException();
            return true;
        }
        return super.onRemoteRequest(code, data, reply, option);
    }
}
;
